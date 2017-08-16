(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('PageUnitController', PageUnitController);

    PageUnitController.$inject = ['PageUnit'];

    function PageUnitController(PageUnit) {

        var vm = this;

        vm.pageUnits = [];

        loadAll();

        function loadAll() {
            PageUnit.query(function(result) {
                vm.pageUnits = result;
                vm.searchQuery = null;
            });
        }
    }
})();
