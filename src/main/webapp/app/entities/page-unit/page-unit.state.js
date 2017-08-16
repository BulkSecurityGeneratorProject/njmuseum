(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('page-unit', {
            parent: 'entity',
            url: '/page-unit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PageUnits'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/page-unit/page-units.html',
                    controller: 'PageUnitController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('page-unit-detail', {
            parent: 'page-unit',
            url: '/page-unit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PageUnit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/page-unit/page-unit-detail.html',
                    controller: 'PageUnitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PageUnit', function($stateParams, PageUnit) {
                    return PageUnit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'page-unit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('page-unit-detail.edit', {
            parent: 'page-unit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page-unit/page-unit-dialog.html',
                    controller: 'PageUnitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PageUnit', function(PageUnit) {
                            return PageUnit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('page-unit.new', {
            parent: 'page-unit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page-unit/page-unit-dialog.html',
                    controller: 'PageUnitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                unitName: null,
                                unitMemo: null,
                                unitTitle: null,
                                unitSort: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('page-unit', null, { reload: 'page-unit' });
                }, function() {
                    $state.go('page-unit');
                });
            }]
        })
        .state('page-unit.edit', {
            parent: 'page-unit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page-unit/page-unit-dialog.html',
                    controller: 'PageUnitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PageUnit', function(PageUnit) {
                            return PageUnit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('page-unit', null, { reload: 'page-unit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('page-unit.delete', {
            parent: 'page-unit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/page-unit/page-unit-delete-dialog.html',
                    controller: 'PageUnitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PageUnit', function(PageUnit) {
                            return PageUnit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('page-unit', null, { reload: 'page-unit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
