(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('UnitContentDialogController', UnitContentDialogController);

    UnitContentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UnitContent', 'PageUnit', 'ContentInfo'];

    function UnitContentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UnitContent, PageUnit, ContentInfo) {
        var vm = this;

        vm.unitContent = entity;
        vm.clear = clear;
        vm.save = save;
        vm.pageunits = PageUnit.query();
        vm.contentinfos = ContentInfo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.unitContent.id !== null) {
                UnitContent.update(vm.unitContent, onSaveSuccess, onSaveError);
            } else {
                UnitContent.save(vm.unitContent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('njmuseumApp:unitContentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
