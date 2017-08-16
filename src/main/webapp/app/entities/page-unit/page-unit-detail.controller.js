(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('PageUnitDetailController', PageUnitDetailController);

    PageUnitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PageUnit', 'Page', 'UnitContent'];

    function PageUnitDetailController($scope, $rootScope, $stateParams, previousState, entity, PageUnit, Page, UnitContent) {
        var vm = this;

        vm.pageUnit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('njmuseumApp:pageUnitUpdate', function(event, result) {
            vm.pageUnit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
