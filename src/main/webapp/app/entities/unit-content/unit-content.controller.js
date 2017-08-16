(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('UnitContentController', UnitContentController);

    UnitContentController.$inject = ['UnitContent'];

    function UnitContentController(UnitContent) {

        var vm = this;

        vm.unitContents = [];

        loadAll();

        function loadAll() {
            UnitContent.query(function(result) {
                vm.unitContents = result;
                vm.searchQuery = null;
            });
        }
    }
})();
