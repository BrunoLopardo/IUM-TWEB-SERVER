app.controller("docentiCtrl", function($rootScope, $scope, $http, CONF) {
    
    $scope.itemsPerPage = 5;
    
    $http({
        url: "MainServlet", 
        method: "POST",
        params: {action: "get_docenti"}
    }).then(function(response) {
            $scope.docenti_data = response.data;
    }, function(response){
        console.log(response);
    });
    
    $scope.showModalName = function(docente){
        $scope.docente = docente;
        $scope.nomeDocente = $scope.docente.nome;
        $scope.cognomeDocente = $scope.docente.cognome;
        $('#editNameModal').modal('show');
    }
  
    $scope.editName = function() {
        if(!$scope.validateEdit()) return;
        
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "change_nome_docente",
                id: $scope.docente.id,
                nome: $scope.nomeDocente,
                cognome: $scope.cognomeDocente
            }
        }).then(function(response) {
                if(response.data.statusCode === 0) {
                    $scope.editDocenteErrorOccurred = false;
                    $scope.docente.nome = $scope.nomeDocente;
                    $scope.docente.cognome = $scope.cognomeDocente;
                    $rootScope.notify("Il nome del docente è stato modificato con successo");
                    $('#editNameModal').modal('hide');
                } else if (response.data.statusCode === 2) {
                    $scope.editDocenteModalErrorMessage = "Nome già utilizzato per un altro docente.";
                    $scope.editDocenteErrorOccurred = true;
                } else {
                    $scope.nuovoDocenteModalErrorMessage = "Si è verificato un errore.";
                    $scope.nuovoDocenteErrorOccurred = true;
                }
        }, function(response){
            $scope.editDocenteModalErrorMessage = "Errore durante la modifica, controllare la connessione a internet.";
            $scope.editDocenteErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.addDocente = function() {
        if(!$scope.validateAdd()) return;
        
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "new_docente",
                nome: $scope.nomeNuovoDocente,
                cognome: $scope.cognomeNuovoDocente
            }
        }).then(function(response) {
                if(response.data.statusCode === 0) {
                    $scope.docenti_data.push({stato: false, nome: $scope.nomeNuovoDocente, cognome: $scope.cognomeNuovoDocente, id: response.data.id});
                    $scope.nomeNuovoDocente = "";
                    $scope.cognomeNuovoDocente = "";
                    $rootScope.notify("Il docente è stato creato con successo");
                    $('#addDocente').modal('hide');
                } else if (response.data.statusCode === 2) {
                    $scope.nuovoDocenteModalErrorMessage = "Nome già utilizzato per un altro docente.";
                    $scope.nuovoDocenteErrorOccurred = true;
                } else {
                    $scope.nuovoDocenteModalErrorMessage = "Si è verificato un errore.";
                    $scope.nuovoDocenteErrorOccurred = true;
                }
        }, function(response){
            $scope.nuovoDocenteModalErrorMessage = "Errore durante la modifica, controllare la connessione a internet.";
            $scope.nuovoDocenteErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.showModalDelete = function(docente){
        $scope.docente = docente;
        $scope.nomeDocente = $scope.docente.nome + $scope.docente.cognome;
        $('#deleteDocente').modal('show');
    }
    
    $scope.deleteDocente = function() {
        $http({
            url: "MainServlet", 
            method: "POST",
            params: {
                action: "delete_docente",
                id: $scope.docente.id
            }
        }).then(function(response) {
            if (response.data.statusCode === 0) {
                $scope.docente.stato = false;
                $rootScope.notify("Il docente è stato eliminato con successo");
                $('#deleteDocente').modal('hide');
            } else {
                $scope.nuovoDocenteModalErrorMessage = "Si è verificato un errore.";
                $scope.nuovoDocenteErrorOccurred = true;
            }
        }, function(response){
            $scope.nuovoDocenteModalErrorMessage = "Errore durante la modifica, controllare la connessione a internet.";
            $scope.nuovoDocenteErrorOccurred = true;
            console.log(response);
        });
    };
    
    $scope.filterDocenti = function(item) {
        if ($scope.search == null) return true;
        
        return item.nome.toLowerCase().includes($scope.search.toLowerCase())
                || item.cognome.toLowerCase().includes($scope.search.toLowerCase())
                || (item.cognome + ' ' + item.nome).toLowerCase().includes($scope.search.toLowerCase())
                || (item.nome + ' ' + item.cognome).toLowerCase().includes($scope.search.toLowerCase());
    };
    
    $scope.validateEdit = function () {
        if ($scope.nomeDocente.length > CONF.maxDocenteNomeLen) {
            $scope.editDocenteModalErrorMessage = "Il nome del docente non può superare i " + CONF.maxDocenteNomeLen + " caratteri.";
            $scope.editDocenteErrorOccurred = true;
            return false;
        }
        
        if ($scope.cognomeDocente.length > CONF.maxDocenteCognomeLen) {
            $scope.editDocenteModalErrorMessage = "Il cognome del docente non può superare i " + CONF.maxDocenteCognomeLen + " caratteri.";
            $scope.editDocenteErrorOccurred = true;
            return false;
        }
        
        return true;
    };
    
    $scope.validateAdd = function () {
        if ($scope.nomeNuovoDocente.length > CONF.maxDocenteNomeLen) {
            $scope.nuovoDocenteModalErrorMessage = "Il nome del docente non può superare i " + CONF.maxDocenteNomeLen + " caratteri.";
            $scope.nuovoDocenteErrorOccurred = true;
            return false;
        }
        
        if ($scope.cognomeNuovoDocente.length > CONF.maxDocenteCognomeLen) {
            $scope.nuovoDocenteModalErrorMessage = "Il cognome del docente non può superare i " + CONF.maxDocenteCognomeLen + " caratteri.";
            $scope.nuovoDocenteErrorOccurred = true;
            return false;
        }
        
        return true;
    };
    
    $('#deleteDocente').on('hidden.bs.modal', function () {
        $scope.eliminaDocenteErrorOccurred = false;
    });
    
    $('#addDocente').on('hidden.bs.modal', function () {
        $scope.nuovoDocenteErrorOccurred = false;
        $scope.nomeNuovoDocente = "";
        $scope.cognomeNuovoDocente = "";
        $scope.$apply();
    });
    
    $('#addDocente').on('shown.bs.modal', function() {
        $('#nomeNuovoDocente').focus();
    });
    
    $('#editNameModal').on('hidden.bs.modal', function () {
        $scope.editDocenteErrorOccurred = false;
        $scope.nomeDocente = "";
        $scope.cognomeDocente = "";
    });
    
    $('#editNameModal').on('shown.bs.modal', function() {
        $('#nomeDocente').focus();
    });
});

