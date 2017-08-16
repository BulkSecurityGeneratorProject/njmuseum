(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('PageDialogController', PageDialogController);

    PageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Page', 'PageUnit'];

    function PageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Page, PageUnit) {
        var vm = this;

        vm.page = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pageunits = PageUnit.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.page.id !== null) {
                Page.update(vm.page, onSaveSuccess, onSaveError);
            } else {
                Page.save(vm.page, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('njmuseumApp:pageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
