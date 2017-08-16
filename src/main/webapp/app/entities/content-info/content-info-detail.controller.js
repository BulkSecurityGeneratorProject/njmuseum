(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('ContentInfoDetailController', ContentInfoDetailController);

    ContentInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ContentInfo', 'UnitContent'];

    function ContentInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, ContentInfo, UnitContent) {
        var vm = this;

        vm.contentInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('njmuseumApp:contentInfoUpdate', function(event, result) {
            vm.contentInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
