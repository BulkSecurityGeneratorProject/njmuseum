(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('PageUnitDeleteController',PageUnitDeleteController);

    PageUnitDeleteController.$inject = ['$uibModalInstance', 'entity', 'PageUnit'];

    function PageUnitDeleteController($uibModalInstance, entity, PageUnit) {
        var vm = this;

        vm.pageUnit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PageUnit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
