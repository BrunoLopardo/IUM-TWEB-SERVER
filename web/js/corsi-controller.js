app.controller("corsiCtrl", function($rootScope, $scope, $http, CONF) {
    
    $scope.itemsPerPage = 5;
    
    $http({
        url: "MainServlet", 
        method: "POST",
        params: {action: "get_corsi"}
    }).then(function(response) {
            $scope.corsi_data = response.data;
    }, function(response){
        console.log(response);
    });
    
    $scope.showModalName = function(corso){
        $scope.corso = corso;
        $scope.nomeCorso = $scope.corso.titolo;
        $('#editNameModal').modal('show');
    }
  
    $scope.editName = function() {
        if (!$scope.validateEdit()) return;
        
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "change_nome_corso",
                id: $scope.corso.id,
                nome: $scope.nomeCorso
            }
        }).then(function(response) {
                if(response.data.statusCode === 0) {
                    $scope.corso.titolo = $scope.nomeCorso;
                    $rootScope.notify("Il nome del corso è stato modificato con successo");
                    $('#editNameModal').modal('hide');
                } else if (response.data.statusCode === 2) {
                    $scope.editCorsoModalErrorMessage = "Nome già utilizzato per un altro corso.";
                    $scope.editCorsoErrorOccurred = true;
                } else {
                    $scope.editCorsoModalErrorMessage = "Si è verificato un errore.";
                    $scope.editCorsoErrorOccurred = true;
                }
        }, function(response){
            $scope.editCorsoModalErrorMessage = "Errore durante la modifica, controllare la connessione a internet.";
            $scope.editCorsoErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.addCorso = function() {
        if (!$scope.validateAdd()) return;
        
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "new_corso",
                nome: $scope.nomeNuovoCorso
            }
        }).then(function(response) {
                if(response.data.statusCode === 0) {
                    $scope.corsi_data.push({stato: false, titolo: $scope.nomeNuovoCorso, id: response.data.id});
                    $rootScope.notify("Il corso è stato creato con successo");
                    $scope.nomeNuovoCorso = "";
                    $('#addCorso').modal('hide');
                } else if (response.data.statusCode === 2) {
                    $scope.nuovoCorsoModalErrorMessage = "Nome già utilizzato per un altro corso.";
                    $scope.nuovoCorsoErrorOccurred = true;
                } else {
                    $scope.nuovoCorsoModalErrorMessage = "Si è verificato un errore.";
                    $scope.nuovoCorsoErrorOccurred = true;
                }
        }, function(response){
            $scope.nuovoCorsoModalErrorMessage = "Errore durante la modifica, controllare la connessione a internet.";
            $scope.nuovoCorsoErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.showModalDelete = function(corso){
        $scope.corso = corso;
        $scope.nomeCorso = $scope.corso.titolo;
        $('#deleteCorso').modal('show');
    }
    
    $scope.deleteCorso = function() {
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "delete_corso",
                id: $scope.corso.id
            }
        }).then(function(response) {
            if(response.data.statusCode === 0) {
                $scope.corso.stato = false;
                $rootScope.notify("Il corso è stato eliminato con successo");
                $('#deleteCorso').modal('hide');
            } else {
                $scope.eliminaCorsoModalErrorMessage = "Si è verificato un errore.";
                $scope.eliminaCorsoErrorOccurred = true;
            }
        }, function(response){
            $scope.eliminaCorsoModalErrorMessage = "Errore durante la modifica, controllare la connessione a internet.";
            $scope.eliminaCorsoErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.validateEdit = function() {
        if ($scope.nomeCorso.length > CONF.maxCorsoTitleLen) {
            $scope.editCorsoModalErrorMessage = "Il nome del corso non può superare i " + CONF.maxCorsoTitleLen + " caratteri.";
            $scope.editCorsoErrorOccurred = true;
            return false;
        }
        
        return true;
    };
    
    $scope.validateAdd = function() {
        if ($scope.nomeNuovoCorso.length > CONF.maxCorsoTitleLen) {
            $scope.nuovoCorsoModalErrorMessage = "Il nome del corso non può superare i " + CONF.maxCorsoTitleLen + " caratteri.";
            $scope.nuovoCorsoErrorOccurred = true;
            return false;
        }
        
        return true;
    };
    
    $('#deleteCorso').on('hidden.bs.modal', function () {
        $scope.eliminaCorsoErrorOccurred = false;
    });
    
    $('#addCorso').on('hidden.bs.modal', function () {
        $scope.nuovoCorsoErrorOccurred = false;
        $scope.nomeNuovoCorso = "";
        $scope.$apply();
    });
    
    $('#addCorso').on('shown.bs.modal', function() {
        $('#nomeNuovoCorso').focus();
    });
    
    $('#editNameModal').on('hidden.bs.modal', function () {
        $scope.editCorsoErrorOccurred = false;
        $scope.nomeCorso = "";
    });
    
    $('#editNameModal').on('shown.bs.modal', function() {
        $('#nomeCorso').focus();
    });
});

