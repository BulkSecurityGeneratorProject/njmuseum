(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('UnitContentDetailController', UnitContentDetailController);

    UnitContentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UnitContent', 'PageUnit', 'ContentInfo'];

    function UnitContentDetailController($scope, $rootScope, $stateParams, previousState, entity, UnitContent, PageUnit, ContentInfo) {
        var vm = this;

        vm.unitContent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('njmuseumApp:unitContentUpdate', function(event, result) {
            vm.unitContent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
