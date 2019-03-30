app.controller("modificaAccountCtrl", function ($scope, $rootScope, $http, $location, CONF) {
    $scope.editAccountErrorOccurred = false;
    $scope.editAccountErrorMessage = "";
    $scope.deleteAccountErrorOccurred = false;
    $scope.deleteAccountErrorMessage = "";

    // Aggiungo il listener per la chiusura del modal
    $('#deleteAccountModal').on('hidden.bs.modal', function () {
        $scope.deleteAccountErrorOccurred = false;
        $scope.deleteAccountErrorMessage = "";
        $scope.deleteAccountModalPassword = "";
        $scope.$apply();
    });
    
    // Listener per autofocus
    $('#deleteAccountModal').on('shown.bs.modal', function() {
        $('#inputPassword').focus();
    });
    
    $scope.editAccount = function () {
        if (!$scope.validateEdit()) return;
        
        $http({
            url: "MainServlet",
            method: "POST",
            params: {
                action: "edit_account",
                /*Username utente loggato*/
                newUsername: $scope.newUsername,
                oldPassword: $scope.oldPassword,
                newPassword: $scope.newPassword
            }
        }).then(function (response) {
            if (response.data.statusCode === 0) {
                $rootScope.login_data = {loggedIn: true, username: $scope.newUsername, isAdmin: $rootScope.login_data.isAdmin};
                $rootScope.notify("Modifica effettuata con successo");
                $scope.editAccountErrorOccurred = false;
                $scope.oldPassword = '';
                $scope.newPassword = '';
                $scope.newUsername = '';
            } else if(response.data.statusCode === 2){
                $scope.editAccountErrorMessage = "Lo username è già in uso da un altro utente.";
                $scope.editAccountErrorOccurred = true;
            } else if(response.data.statusCode === 4){
                $scope.editAccountErrorMessage = "La vecchia password non corrisponde a quella in uso.";
                $scope.editAccountErrorOccurred = true;
            } else {
                $scope.newPrenotazioneModalErrorMessage = "Si è verificato un errore.";
                $scope.newPrenotazioneErrorOccurred = true;
            }
        }, function (response) {
            $scope.editAccountErrorMessage = "Errore durante il login, controllare la connessione a internet.";
            $scope.editAccountErrorOccurred = true;
            console.log(response);
        });
    };

    $scope.deleteAccount = function () {
        if(!$scope.validateDelete()) return;
        
        $http({
            url: "MainServlet",
            method: "POST",
            params: {
                action: "delete_account",
                /*Username utente loggato*/
                password: $scope.deleteAccountModalPassword
            }
        }).then(function (response) {
            if (response.data.statusCode === 0) {
                $rootScope.login_data = {loggedIn: false};
                $rootScope.notify("Eliminazione effettuata con successo");
                $('#deleteAccountModal').modal('hide');
                $location.path("/");
            } else if (response.data.statusCode === 2) {
                $scope.deleteAccountErrorMessage = "Password errata.";
                $scope.deleteAccountErrorOccurred = true;
            } else {
                $scope.deleteAccountErrorMessage = "Si è verificato un errore.";
                $scope.deleteAccountErrorOccurred = true;
            }
        }, function (response) {
            $scope.deleteAccountErrorMessage = "Errore durante il login, controllare la connessione a internet.";
            $scope.deleteAccountErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.validateEdit = function () {
        if ($scope.newUsername.length > CONF.maxUsernameLen) {
            $scope.editAccountErrorMessage = "Lo username non può superare i " + CONF.maxUsernameLen + " caratteri.";
            $scope.editAccountErrorOccurred = true;
            return false;
        }
        
        if ($scope.oldPassword.length > CONF.maxPswLen || $scope.newPassword.length > CONF.maxPswLen) {
            $scope.editAccountErrorMessage = "Le password non può superare i " + CONF.maxPswLen + " caratteri.";
            $scope.editAccountErrorOccurred = true;
            return false;
        }
        
        return true;
    };
    
    $scope.validateDelete = function () {
        if ($scope.deleteAccountModalPassword.length > CONF.maxPswLen) {
            $scope.deleteAccountErrorMessage = "La password non può superare i " + CONF.maxPswLen + " caratteri.";
            $scope.deleteAccountErrorOccurred = true;
            return false;
        }
        
        return true;
    };
});

