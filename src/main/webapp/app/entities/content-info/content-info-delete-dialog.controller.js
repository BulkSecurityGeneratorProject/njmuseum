(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('ContentInfoDeleteController',ContentInfoDeleteController);

    ContentInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ContentInfo'];

    function ContentInfoDeleteController($uibModalInstance, entity, ContentInfo) {
        var vm = this;

        vm.contentInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ContentInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
