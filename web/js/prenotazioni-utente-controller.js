app.controller("prenotazioniUtenteCtrl", function ($rootScope, $scope, $http) {
    $scope.itemsPerPage = 5; 
    
    $scope.cancelReservationErrorOccurred = false;
    $scope.cancelReservationErrorMessage = "";

    // Aggiungo il listener per la chiusura del modal
    $('#cancelReservationModal').on('hidden.bs.modal', function () {
        $scope.cancelReservationErrorOccurred = false;
        $scope.cancelReservationErrorMessage = "";
        $scope.$apply();
    });

    $http({
        url: "MainServlet",
        method: "POST",
        params: {action: "get_prenotazioni_utente"}
    }).then(function (response) {
        $scope.prenotazioni_utente_data = response.data;
    }, function (response) {
        console.log(response);
    });
    
    $scope.showModal = function(prenotazione){
        $scope.prenotazione = prenotazione;
        $('#cancelReservationModal').modal('show');
    };
    
    $scope.filterCorsoDocente = function(item) {
        if ($scope.search == null) return true;
        
        return item.docente.nome.toLowerCase().includes($scope.search.toLowerCase())
                || item.docente.cognome.toLowerCase().includes($scope.search.toLowerCase())
                || (item.docente.cognome + ' ' + item.docente.nome).toLowerCase().includes($scope.search.toLowerCase())
                || (item.docente.nome + ' ' + item.docente.cognome).toLowerCase().includes($scope.search.toLowerCase())
                || item.corso.titolo.toLowerCase().includes($scope.search.toLowerCase());
    };

    $scope.cancelReservation = function () {
        $http({
            url: "MainServlet",
            method: "POST",
            params: {
                action: "cancel_prenotazioni_utente",
                idPrenotazione: $scope.prenotazione.id
            }
        }).then(function (response) {
            if (response.data.statusCode === 0) {
                $rootScope.notify("Prenotazione disdetta con successo");
                $scope.prenotazione.stato = 'DISDETTA';
                $('#cancelReservationModal').modal('hide'); 
            } else {
                $scope.cancelReservationErrorMessage = "Si è verificato un errore.";
                $scope.cancelReservationErrorOccurred = true;
            }
        }, function (response) {
            $scope.cancelReservationErrorMessage = "Errore durante il login, controllare la connessione a internet.";
            $scope.cancelReservationErrorOccurred = true;
            console.log(response);
        });
    };
});


