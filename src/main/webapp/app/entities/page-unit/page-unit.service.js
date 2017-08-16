(function() {
    'use strict';
    angular
        .module('njmuseumApp')
        .factory('PageUnit', PageUnit);

    PageUnit.$inject = ['$resource'];

    function PageUnit ($resource) {
        var resourceUrl =  'api/page-units/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
