(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('unit-content', {
            parent: 'entity',
            url: '/unit-content',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UnitContents'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unit-content/unit-contents.html',
                    controller: 'UnitContentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('unit-content-detail', {
            parent: 'unit-content',
            url: '/unit-content/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UnitContent'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unit-content/unit-content-detail.html',
                    controller: 'UnitContentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UnitContent', function($stateParams, UnitContent) {
                    return UnitContent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'unit-content',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('unit-content-detail.edit', {
            parent: 'unit-content-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit-content/unit-content-dialog.html',
                    controller: 'UnitContentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UnitContent', function(UnitContent) {
                            return UnitContent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unit-content.new', {
            parent: 'unit-content',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit-content/unit-content-dialog.html',
                    controller: 'UnitContentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contentName: null,
                                contentMemo: null,
                                contentSort: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('unit-content', null, { reload: 'unit-content' });
                }, function() {
                    $state.go('unit-content');
                });
            }]
        })
        .state('unit-content.edit', {
            parent: 'unit-content',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit-content/unit-content-dialog.html',
                    controller: 'UnitContentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UnitContent', function(UnitContent) {
                            return UnitContent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('unit-content', null, { reload: 'unit-content' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unit-content.delete', {
            parent: 'unit-content',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unit-content/unit-content-delete-dialog.html',
                    controller: 'UnitContentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UnitContent', function(UnitContent) {
                            return UnitContent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('unit-content', null, { reload: 'unit-content' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
