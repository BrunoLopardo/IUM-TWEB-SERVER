app.controller("homeCtrl", function ($rootScope, $scope, $http, CONF) {

    $scope.itemsPerPage = 15;
    $scope.view_tab = 0;

    $http({
        url: "MainServlet",
        method: "POST",
        params: {action: "get_ripetizioni_disponibili"}
    }).then(function (response) {
        $scope.ripetizioni_data = response.data;
    }, function (response) {
        console.log(response);
    });

    $scope.showModal = function (ora, day, data) {
        $scope.data = data;
        $scope.ora = ora;
        $scope.giorno = day;
        $scope.idCorsoSelect = data[0].corso.id;
        $scope.view_tab = 0;
        $scope.docenteSelection = '0';
        $('#addPrenotazione').modal('show');

    };

    $scope.changeTab = function (index, corso) {
        $scope.view_tab = index;
        $scope.idCorsoSelect = corso;
        $scope.docenteSelection = '0';
    };
    
    $scope.addPrenotazione = function() {
        if(!$scope.validateAdd()) return;
        
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "new_prenotazione",
                idDocente: $scope.docenteSelection,
                idCorso: $scope.idCorsoSelect,
                ora: $scope.ora,
                day: $scope.giorno
            }
        }).then(function(response) {
                if(response.data.statusCode === 0) {
                    // Rimuovo la ripetizione dal calendario
                    $scope.ripetizioni_data = $scope.ripetizioni_data.filter(function(obj) {
                        return obj.docente.id != $scope.docenteSelection
                                || obj.giorno != $scope.giorno
                                || obj.oraInizio != $scope.ora;
                    });
                    $scope.newPrenotazioneErrorOccurred = false;
                    $rootScope.notify("Prenotazione effettuata");
                    $('#addPrenotazione').modal('hide');
                } else if (response.data.statusCode === 3) {
                    $scope.newPrenotazioneModalErrorMessage = "\
                        Sessione scaduta, devi effettuare di nuovo il \n\
                        <a href=\"#loginModal\" data-toggle=\"modal\" data-dismiss=\"modal\" class=\"alert-link\">Login</a>";
                    $scope.newPrenotazioneErrorOccurred = true;
                    $rootScope.login_data = { loggedIn: false };
                } else if (response.data.statusCode === 5) {
                    $scope.newPrenotazioneModalErrorMessage = "La ripetizione scelta non è più disponibile.";
                    $scope.newPrenotazioneErrorOccurred = true;
                }else if (response.data.statusCode === 6) {  
                    $scope.newPrenotazioneModalErrorMessage = "Hai già una prenotazione per il " + $scope.giorno + " alle " + $scope.ora + ":00.";
                    $scope.newPrenotazioneErrorOccurred = true;
                } else if (response.data.statusCode === 7) {
                    $scope.newPrenotazioneModalErrorMessage = "Il docente selezionato non è disponibile il " + $scope.giorno + " alle " + $scope.ora + ":00.";
                    $scope.newPrenotazioneErrorOccurred = true;
                } else {
                    $scope.newPrenotazioneModalErrorMessage = "Si è verificato un errore.";
                    $scope.newPrenotazioneErrorOccurred = true;
                }
        }, function(response){
            $scope.newPrenotazioneModalErrorMessage = "Errore durante la modifica, controllare la connessione a internet.";
            $scope.newPrenotazioneErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.validateAdd = function() {
        if (!$rootScope.login_data.loggedIn) {
            $scope.newPrenotazioneModalErrorMessage = "\
                        Devi effettuare il \n\
                        <a href=\"#loginModal\" data-toggle=\"modal\" data-dismiss=\"modal\" class=\"alert-link\">Login</a>\n\
                        per potere effettuare la prenotazione.";
            $scope.newPrenotazioneErrorOccurred = true;
            return false;
        }
        return true;
    };
    
    $('#addPrenotazione').on('hidden.bs.modal', function () {
        $scope.newPrenotazioneErrorOccurred = false;
    });

});

app.filter('unique', function () {
    return function (collection) {
        var output = [],
                keys = [];
        angular.forEach(collection, function (item) {
            var key = item.corso.titolo;
            if (keys.indexOf(key) === -1) {
                keys.push(key);
                output.push(item);
            }
        });
        return output;
    };
});

app.filter('unsafe', function($sce) {
    return function(value) {
        if (!value) { return ''; }
        return $sce.trustAsHtml(value);
    };
});