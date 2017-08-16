'use strict';

describe('Controller Tests', function() {

    describe('PageUnit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPageUnit, MockPage, MockUnitContent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPageUnit = jasmine.createSpy('MockPageUnit');
            MockPage = jasmine.createSpy('MockPage');
            MockUnitContent = jasmine.createSpy('MockUnitContent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PageUnit': MockPageUnit,
                'Page': MockPage,
                'UnitContent': MockUnitContent
            };
            createController = function() {
                $injector.get('$controller')("PageUnitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'njmuseumApp:pageUnitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
