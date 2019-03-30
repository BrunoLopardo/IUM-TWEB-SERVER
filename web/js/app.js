var app = angular.module("appRipetizioni", ["ngRoute"]);

app.config(function ($routeProvider, $locationProvider) {
    $routeProvider
            .when("/", {
                templateUrl: "home.html",
                controller : "homeCtrl"
            })
            
            .when("/corsi", {
                templateUrl: "corsi.html",
                controller : "corsiCtrl"
            })
            
            .when("/docenti", {
                templateUrl: "docenti.html",
                controller : "docentiCtrl"
            })
            
            .when("/prenotazioni", {
                templateUrl: "prenotazioni.html",
                controller : "prenotazioniCtrl"
            })
            
            .when("/docenze", {
                templateUrl: "docenze.html",
                controller : "docenzeCtrl"
            })
            
            .when("/utenti", {
                templateUrl: "utenti.html",
                controller : "utentiCtrl"
            })
            
            .when("/modificaAccount", {
                templateUrl: "modifica-account.html",
                controller : "modificaAccountCtrl"
            })
            
            .when("/prenotazioniUtente", {
                templateUrl: "prenotazioni-utente.html",
                controller : "prenotazioniUtenteCtrl" 
            })
            
            .otherwise({
                templateUrl: "404-not-found.html"
            });
            
    $locationProvider.html5Mode(true);
});

app.run(function($rootScope, $location, $http) {
    $rootScope.$on("$routeChangeStart", function($event, next, current) {
        
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {action: "logged_in"}
        }).then(function(response) {
                $rootScope.login_data = response.data;
                
                if ( response.data.loggedIn === false ) {
                    $location.path( "/" );
                }
        }, function(response){
            console.log(response);
        });  
    });
});

app.constant("CONF", {
    maxPswLen: 128,
    maxUsernameLen: 20,
    maxCorsoTitleLen: 100,
    maxDocenteNomeLen: 20,
    maxDocenteCognomeLen: 20
});

app.controller("mainCtrl", function($scope, $rootScope, $http, $location, CONF) {
    
    $scope.loginErrorOccurred = false;
    $scope.loginErrorMessage = "";
    
    $rootScope.dayConverter = function(day) {
        
        switch(day){
            case "LUN":
                return "Lunedì";
                break;
            case "MAR":
                return "Martedì";
                break;
            case "MER":
                return "Mercoledì";
                break;
            case "GIO":
                return "Giovedì";
                break;
            case "VEN":
                return "Venerdì";
                break;
            default:
                return null;
                break;
        } 
    };
    
    // Aggiungo il listener per la chiusura del modal
    $('#loginModal').on('hidden.bs.modal', function() {
        $scope.loginErrorOccurred = false;
        $scope.loginErrorMessage = "";
        $scope.loginModalUsername = "";
        $scope.loginModalPassword = "";
        $scope.$apply();
    });
    
    // Aggiungo il listener per la apertura del modal
    $('#loginModal').on('shown.bs.modal', function() {
        $('#usernameInput').focus();
    });
    
    $http({
        url: "MainServlet", 
        method: "POST",
        params: {action: "logged_in"}
    }).then(function(response) {
            $rootScope.login_data = response.data;
    }, function(response){
        console.log(response);
    });
    
    $rootScope.windowWidth = $(window).width();
    $(window).resize(function() {
        $rootScope.windowWidth = $(window).width();
        $rootScope.$apply();
    });
    
    $rootScope.notify = function(text, delay) {
        delay = delay || 2000;
        
        $(".notify").toggleClass("active-notify");
        $(".notify").text(text);

        setTimeout(function(){
            $(".notify").removeClass("active-notify");
        }, delay);
    };
    
    $scope.logout = function() {
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {action: "logout"}
        }).then(function(response) {
                $rootScope.login_data = { loggedIn: false };
                $location.path("/");
                $rootScope.notify("Logout avvenuto con successo!");
        }, function(response){
            console.log(response);
        });
    };
    
    $scope.login = function() {
        if (!$scope.validate()) return;
        
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "login",
                username: $scope.loginModalUsername,
                password: $scope.loginModalPassword
            }
        }).then(function(response) {
                $rootScope.login_data = response.data;
                
                if ($rootScope.login_data.loggedIn === false) {
                    $scope.loginErrorMessage = "Username e/o password errati.";
                    $scope.loginErrorOccurred = true;
                } else {
                    $rootScope.notify("Benvenuto " + response.data.username);
                    $('#loginModal').modal('hide');
                }
        }, function(response){
            $scope.loginErrorMessage = "Errore durante il login, controllare la connessione a internet.";
            $scope.loginErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.signup = function() {
        if (!$scope.validate()) return;
        
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "signup",
                username: $scope.loginModalUsername,
                password: $scope.loginModalPassword
            }
        }).then(function(response) {
                
                if(response.data.statusCode === 0) {
                    $rootScope.login_data = { loggedIn: true, username: $scope.loginModalUsername, isAdmin: false };
                    $rootScope.notify("Benvenuto " + $scope.loginModalUsername);
                    $('#loginModal').modal('hide');
                } else if (response.data.statusCode === 2) {
                    $scope.loginErrorMessage = "Lo username è già in uso da un altro utente.";
                    $scope.loginErrorOccurred = true;
                } else {
                    $scope.loginErrorMessage = "Si è verificato un errore.";
                    $scope.loginErrorOccurred = true;
                }
        }, function(response){
            $scope.loginErrorMessage = "Errore durante il login, controllare la connessione a internet.";
            $scope.loginErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.validate = function() {
        if ($scope.loginModalUsername.length > CONF.maxUsernameLen) {
            $scope.loginErrorMessage = "Lo username non può superare i " + CONF.maxUsernameLen + " caratteri.";
            $scope.loginErrorOccurred = true;
            return false;
        }
        
        if ($scope.loginModalPassword.length > CONF.maxPswLen) {
            $scope.loginErrorMessage = "La password non può superare i " + CONF.maxPswLen + " caratteri.";
            $scope.loginErrorOccurred = true;
            return false;
        }
        
        return true;
    };
});
