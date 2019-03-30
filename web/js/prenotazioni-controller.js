app.controller("prenotazioniCtrl", function($scope, $http) {
    
    $scope.itemsPerPage = 5;
    
    $http({
        url: "MainServlet", 
        method: "POST",
        params: {action: "get_prenotazioni"}
    }).then(function(response) {
            $scope.utenti_data = response.data;
    }, function(response){
        console.log(response);
    });
    
    
});

