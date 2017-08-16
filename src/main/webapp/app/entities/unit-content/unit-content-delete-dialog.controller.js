(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('UnitContentDeleteController',UnitContentDeleteController);

    UnitContentDeleteController.$inject = ['$uibModalInstance', 'entity', 'UnitContent'];

    function UnitContentDeleteController($uibModalInstance, entity, UnitContent) {
        var vm = this;

        vm.unitContent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UnitContent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
