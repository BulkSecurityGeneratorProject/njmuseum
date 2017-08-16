(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('content-info', {
            parent: 'entity',
            url: '/content-info',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ContentInfos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/content-info/content-infos.html',
                    controller: 'ContentInfoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('content-info-detail', {
            parent: 'content-info',
            url: '/content-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ContentInfo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/content-info/content-info-detail.html',
                    controller: 'ContentInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ContentInfo', function($stateParams, ContentInfo) {
                    return ContentInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'content-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('content-info-detail.edit', {
            parent: 'content-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/content-info/content-info-dialog.html',
                    controller: 'ContentInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContentInfo', function(ContentInfo) {
                            return ContentInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('content-info.new', {
            parent: 'content-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/content-info/content-info-dialog.html',
                    controller: 'ContentInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                infoTitle: null,
                                infoCover: null,
                                infoText: null,
                                infoImage: null,
                                infoSort: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('content-info', null, { reload: 'content-info' });
                }, function() {
                    $state.go('content-info');
                });
            }]
        })
        .state('content-info.edit', {
            parent: 'content-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/content-info/content-info-dialog.html',
                    controller: 'ContentInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ContentInfo', function(ContentInfo) {
                            return ContentInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('content-info', null, { reload: 'content-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('content-info.delete', {
            parent: 'content-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/content-info/content-info-delete-dialog.html',
                    controller: 'ContentInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ContentInfo', function(ContentInfo) {
                            return ContentInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('content-info', null, { reload: 'content-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
