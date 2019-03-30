function PaginationController($rootScope, $scope) {
    var $ctrl = this;
    
    $ctrl.$onInit = function() {
        if ($ctrl.itemCount == null) $ctrl.itemCount = 0;
        if ($ctrl.itemsPerPage == null || $ctrl.itemsPerPage < 1) $ctrl.itemsPerPage = 1;
        $ctrl.defaultDelta = $ctrl.delta || 3;
        $ctrl.deltaSm = $ctrl.deltaSm || 1;
        $scope.breakpoint = $ctrl.breakpoint || 576;

        $scope.currentPage = 1;
        $scope.pageCount = Math.ceil($ctrl.itemCount / $ctrl.itemsPerPage);
        
        if ($scope.pageCount < 1) {
            $scope.pageCount = 1;
        }

        $ctrl.beginIndex = 0;
        
        // Impedisco agli href="#" di cambiare l'url (altrimenti si ricarica la pagina)
        $('.prevent-default').click(function(event){
            event.preventDefault();
        });
        
        // Registro il listener dell'evento qui poichè $onChanges viene chiamato
        // prima di $onInit.
        $ctrl.$onChanges = $ctrl.onChanges;
    }

    $ctrl.onChanges = function(changes) {
        
        if ($ctrl.itemCount == null) $ctrl.itemCount = 0;
        if ($ctrl.itemsPerPage == null || $ctrl.itemsPerPage < 1) $ctrl.itemsPerPage = 1;

        $scope.pageCount = Math.ceil($ctrl.itemCount / $ctrl.itemsPerPage);
        
        if ($scope.pageCount < 1) {
            $scope.pageCount = 1;
        }

        if ($scope.currentPage > $scope.pageCount) {
            $scope.currentPage = $scope.pageCount;
        }

        if ($scope.currentPage < 1) {
            $scope.currentPage = 1;
        }

        $ctrl.beginIndex = $ctrl.itemsPerPage * ($scope.currentPage - 1);
    }

    $scope.next = function () {
        $scope.currentPage += 1;

        if ($scope.currentPage > $scope.pageCount) {
            $scope.currentPage = $scope.pageCount;
        }

        $ctrl.beginIndex = $ctrl.itemsPerPage * ($scope.currentPage - 1);
    };

    $scope.setPage = function (p) {
        if (p === '...') return;
        if ($scope.currentPage >= 1 || $scope.currentPage <= $scope.pageCount) {
            $scope.currentPage = p;
            $ctrl.beginIndex = $ctrl.itemsPerPage * ($scope.currentPage - 1);
        }
    };

    $scope.prev = function () {
        $scope.currentPage -= 1;

        if ($scope.currentPage < 1) {
            $scope.currentPage = 1;
        }

        $ctrl.beginIndex = $ctrl.itemsPerPage * ($scope.currentPage - 1);
    };

    $scope.pagination = function(curPage, pageCount) {
        var delta = $ctrl.defaultDelta;

        if ($rootScope.windowWidth < $scope.breakpoint) {
            delta = $ctrl.deltaSm;
        }

        var range = [];
        var rangeWithDots = [];
        var k;
      
        range.push(1)  
        for (let i = curPage - delta; i <= curPage + delta; i++) {
            if (i < pageCount && i > 1) {
                range.push(i);
            }
        }  
    
        if (pageCount != 1) range.push(pageCount);
    
        for (let i of range) {
            if (k) {
                if (i - k === 2) {
                    rangeWithDots.push(k + 1);
                } else if (i - k !== 1) {
                    rangeWithDots.push('...');
                }
            }
            rangeWithDots.push(i);
            k = i;
        }
    
        return rangeWithDots;
    };
};

app.component('paginationControls', {
    templateUrl: 'componenti/paginazione/paginazione.html',
    controller: PaginationController,
    bindings: {
        itemCount: '<',
        itemsPerPage: '<',
        delta: '<?',
        deltaSm: '<?',
        breakpoint: '<?',
        beginIndex: '='
    }
});