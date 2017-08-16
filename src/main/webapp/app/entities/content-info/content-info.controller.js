(function() {
    'use strict';

    angular
        .module('njmuseumApp')
        .controller('ContentInfoController', ContentInfoController);

    ContentInfoController.$inject = ['ContentInfo'];

    function ContentInfoController(ContentInfo) {

        var vm = this;

        vm.contentInfos = [];

        loadAll();

        function loadAll() {
            ContentInfo.query(function(result) {
                vm.contentInfos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
