(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('PageUnitDialogController', PageUnitDialogController);

    PageUnitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PageUnit', 'Page', 'UnitContent'];

    function PageUnitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PageUnit, Page, UnitContent) {
        var vm = this;

        vm.pageUnit = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pages = Page.query();
        vm.unitcontents = UnitContent.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pageUnit.id !== null) {
                PageUnit.update(vm.pageUnit, onSaveSuccess, onSaveError);
            } else {
                PageUnit.save(vm.pageUnit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('njmuseumApp:pageUnitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
