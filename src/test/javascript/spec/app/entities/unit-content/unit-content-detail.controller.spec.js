'use strict';

describe('Controller Tests', function() {

    describe('UnitContent Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUnitContent, MockPageUnit, MockContentInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUnitContent = jasmine.createSpy('MockUnitContent');
            MockPageUnit = jasmine.createSpy('MockPageUnit');
            MockContentInfo = jasmine.createSpy('MockContentInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UnitContent': MockUnitContent,
                'PageUnit': MockPageUnit,
                'ContentInfo': MockContentInfo
            };
            createController = function() {
                $injector.get('$controller')("UnitContentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'njmuseumApp:unitContentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
