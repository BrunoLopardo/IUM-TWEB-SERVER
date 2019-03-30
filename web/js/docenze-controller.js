app.controller("docenzeCtrl", function($rootScope, $scope, $http) {
    $scope.itemsPerPage = 5;
    
    $http({
        url: "MainServlet", 
        method: "POST",
        params: {action: "get_docenze"}
    }).then(function(response) {
            $scope.docenze_data = response.data;
    }, function(response){
        console.log(response);
    });
    
    $http({
        url: "MainServlet", 
        method: "POST",
        params: {action: "get_corsi"}
    }).then(function(response) {
            $scope.corsi_data = response.data;
    }, function(response){
        console.log(response);
    });
    
    $http({
        url: "MainServlet", 
        method: "POST",
        params: {action: "get_docenti"}
    }).then(function(response) {
            $scope.docenti_data = response.data;
    }, function(response){
        console.log(response);
    });
    
    $scope.filterDocenze = function(item) {
        if ($scope.search == null) return true;
        
        return item.docente.nome.toLowerCase().includes($scope.search.toLowerCase())
                || item.docente.cognome.toLowerCase().includes($scope.search.toLowerCase())
                || (item.docente.cognome + ' ' + item.docente.nome).toLowerCase().includes($scope.search.toLowerCase())
                || (item.docente.nome + ' ' + item.docente.cognome).toLowerCase().includes($scope.search.toLowerCase())
                || item.corso.titolo.toLowerCase().includes($scope.search.toLowerCase());
    };
    
    $scope.showModalDelete = function(docenza){
        $scope.docenza = docenza;
        $scope.nomeDocenza = $scope.docenza.corso.titolo;
        $('#deleteDocenza').modal('show');
    };
    
    // Necessario per safari
    $scope.showModalAdd = function(docenza){
        $scope.docente = '0';
        $scope.corso = '0';
        $('#addDocenza').modal('show');
    };
    
    $scope.deleteDocenza = function() {
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "delete_docenza",
                idDocente: $scope.docenza.docente.id,
                idCorso: $scope.docenza.corso.id
            }
        }).then(function(response) {
            if(response.data.statusCode === 0) {
                $scope.docenze_data = $scope.docenze_data.filter(function(obj){
                    if (obj.corso.id != $scope.docenza.corso.id || obj.docente.id != $scope.docenza.docente.id) {
                      return true;
                    }
                    return false;
                });
                $rootScope.notify("La docenza è stata eliminata con successo");
                $('#deleteDocenza').modal('hide');
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
    
    $scope.addDocenza = function() {
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "new_docenza",
                idDocente: $scope.docente,
                idCorso: $scope.corso
            }
        }).then(function(response) {
                if(response.data.statusCode === 0) {
                    var docente = $scope.docenti_data.find(obj => {
                        return obj.id.toString() === $scope.docente;
                    });
                    var corso = $scope.corsi_data.find(obj => {
                        return obj.id.toString() === $scope.corso;
                    });
                    $scope.docenze_data.push({corso: corso, docente: docente});
                    $scope.docente = '0';
                    $scope.corso = '0';
                    $rootScope.notify("La docenza è stata creata con successo");
                    $('#addDocenza').modal('hide');
                } else if (response.data.statusCode === 2) {
                    $scope.nuovaDocenzaModalErrorMessage = "Docenza già presente.";
                    $scope.nuovaDocenzaErrorOccurred = true;
                } else {
                    $scope.nuovaDocenzaModalErrorMessage = "Si è verificato un errore.";
                    $scope.nuovaDocenzaErrorOccurred = true;
                }
        }, function(response){
            $scope.nuovaDocenzaModalErrorMessage = "Errore durante la modifica, controllare la connessione a internet.";
            $scope.nuovaDocenzaErrorOccurred = true;
            console.log(response);
        });
    };
    
    $('#deleteDocenza').on('hidden.bs.modal', function () {
        $scope.eliminaCorsoErrorOccurred = false;
        $scope.$apply();
    });
    
   $('#addDocenza').on('hidden.bs.modal', function () {
        $scope.nuovaDocenzaErrorOccurred = false;
        $scope.docente = '0';
        $scope.corso = '0';
        $scope.$apply();
    });
});

