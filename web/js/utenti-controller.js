app.controller("utentiCtrl", function($rootScope, $scope, $http, CONF) {
    
    $scope.itemsPerPage = 5;
    
    $scope.deleteUserErrorOccurred = false;
    $scope.deleteUserErrorMessage = "";
    $scope.editPasswordErrorOccurred = false;
    $scope.editPasswordErrorMessage = "";
    $scope.changeRoleUserErrorOccurred = false;
    $scope.changeRoleUserErrorMessage = "";
   
    // Aggiungo i listener per la chiusura dei modal
    $('#promoteUser').on('hidden.bs.modal', function () {
        $scope.changeRoleUserErrorOccurred = false;
        $scope.changeRoleUserErrorMessage = "";
        $scope.$apply();
    });
    
    $('#editUser').on('hidden.bs.modal', function () {
        $scope.editPasswordErrorOccurred = false;
        $scope.editPasswordErrorMessage = "";
        $scope.nuovaPasswordUtente = "";
        $scope.$apply();
    });
    
    $('#deleteUser').on('hidden.bs.modal', function () {
        $scope.deleteUserErrorOccurred = false;
        $scope.deleteUserErrorMessage = "";
        $scope.$apply();
    });
    
    $http({
        url: "MainServlet", 
        method: "POST",
        params: {action: "get_utenti"}
    }).then(function(response) {
            $scope.utenti_data = response.data;
    }, function(response){
        console.log(response);
    });
    
    $scope.showModalPromote = function(utente){
        $scope.utente = utente;
        $('#promoteUser').modal('show');
    };
    
    $scope.showModalEdit = function(utente){
        $scope.utente = utente;
        $('#editUser').modal('show');
    };
    
    // Listener per autofocus
    $('#editUser').on('shown.bs.modal', function() {
        $('#nuovaPasswordUtente').focus();
    });
    
    
    $scope.showModalDelete = function(utente){
        $scope.utente = utente;
        $('#deleteUser').modal('show');
    };
    
    $scope.deleteUser = function () {

        $http({
            url: "MainServlet",
            method: "POST",
            params: {
                action: "delete_utente",
                id: $scope.utente.id
            }

        }).then(function (response) {
            if (response.data.statusCode === 0) {
                $scope.utenti_data = $scope.utenti_data.filter(function(obj) {
                    return obj.id != $scope.utente.id;
                });
                $rootScope.notify("Utente eliminato");
                $('#deleteUser').modal('hide'); 
            } else {
                $scope.deleteUserErrorMessage = "Si è verificato un errore.";
                $scope.deleteUserErrorOccurred = true;
            }
            
        }, function (response) {
            $scope.deleteUserErrorMessage = "Errore durante il login, controllare la connessione a internet.";
            $scope.deleteUserErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.editPassword = function () {
        if(!$scope.validateEdit()) return;

        $http({
            url: "MainServlet",
            method: "POST",
            params: {
                action: "edit_password_utente",
                newPassword: $scope.nuovaPasswordUtente,
                id: $scope.utente.id
            }

        }).then(function (response) {
            if (response.data.statusCode === 0) {
                $rootScope.notify("Password modificata");
                $('#editUser').modal('hide');
            } else {
                $scope.editPasswordErrorMessage = "Si è verificato un errore.";
                $scope.editPasswordErrorOccurred = true;
            }          
        }, function (response) {
            $scope.editPasswordErrorMessage = "Errore durante il login, controllare la connessione a internet.";
            $scope.editPasswordErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.changeRoleUser = function () {

        $http({
            url: "MainServlet",
            method: "POST",
            params: {
                action: "change_role_utente",
                id: $scope.utente.id
            }

        }).then(function (response) {
            if (response.data.statusCode === 0) {
                $scope.utente.isAdmin = !$scope.utente.isAdmin;
                $rootScope.notify("Cambio ruolo effettuato");
                $('#promoteUser').modal('hide'); 
            } else {
                $scope.changeRoleUserErrorMessage = "Si è verificato un errore.";
                $scope.changeRoleUserErrorOccurred = true;
            }          
        }, function (response) {
            $scope.changeRoleUserErrorMessage = "Errore durante il login, controllare la connessione a internet.";
            $scope.changeRoleUserErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.validateEdit = function () {
        if ($scope.nuovaPasswordUtente.length > CONF.maxPswLen) {
            $scope.editPasswordErrorMessage = "La password non può superare i " + CONF.maxPswLen + " caratteri.";
            $scope.editPasswordErrorOccurred = true;
            return false;
        }
        
        return true;
    };
});