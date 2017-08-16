(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('ContentInfoDialogController', ContentInfoDialogController);

    ContentInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ContentInfo', 'UnitContent'];

    function ContentInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ContentInfo, UnitContent) {
        var vm = this;

        vm.contentInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.unitcontents = UnitContent.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.contentInfo.id !== null) {
                ContentInfo.update(vm.contentInfo, onSaveSuccess, onSaveError);
            } else {
                ContentInfo.save(vm.contentInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('njmuseumApp:contentInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
