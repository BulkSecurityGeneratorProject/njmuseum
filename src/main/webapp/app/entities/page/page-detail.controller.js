(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('PageDetailController', PageDetailController);

    PageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Page', 'PageUnit'];

    function PageDetailController($scope, $rootScope, $stateParams, previousState, entity, Page, PageUnit) {
        var vm = this;

        vm.page = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('njmuseumApp:pageUpdate', function(event, result) {
            vm.page = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
