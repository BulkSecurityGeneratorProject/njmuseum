(function() {
    'use strict';
    angular
        .module('njmuseumApp')
        .factory('ContentInfo', ContentInfo);

    ContentInfo.$inject = ['$resource'];

    function ContentInfo ($resource) {
        var resourceUrl =  'api/content-infos/:id';

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
