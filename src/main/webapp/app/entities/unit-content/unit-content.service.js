(function() {
    'use strict';
    angular
        .module('njmuseumApp')
        .factory('UnitContent', UnitContent);

    UnitContent.$inject = ['$resource'];

    function UnitContent ($resource) {
        var resourceUrl =  'api/unit-contents/:id';

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
