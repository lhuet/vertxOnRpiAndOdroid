angular.module('demo-breizhcamp', [])
    .controller('MainCtrl', ['$scope', '$http', '$timeout', function($scope, $http, $timeout) {

        $scope.eb = null;

        $http.get('/temp/1')
            .then(function success(response){
                $scope.temp1 = response.data;
            }, function failure(response){
                console.log("Error " + response.status + " : " + response.data)
            });

        $http.get('/temp/2')
            .then(function success(response){
                $scope.temp2 = response.data;
            }, function failure(response){
                console.log("Error " + response.status + " : " + response.data)
            });


        $timeout(function() {
            if ($scope.eb != null) {
                $scope.eb.close();
            }

            $scope.eb = new EventBus(window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/eventbus');

            $scope.eb.onopen = function() {

                $scope.eb.registerHandler("sensor-temp-1", function (err, msg) {
                    if (err) {
                        console.error('Failed to retrieve temp 1 : ' + err);
                        return;
                    }
                    console.log("temp 1 : " + msg.body );
                    $scope.temp1 = msg.body;
                    $scope.$apply();
                });

                $scope.eb.registerHandler("sensor-temp-2", function (err, msg) {
                    if (err) {
                        console.error('Failed to retrieve temp 2 : ' + err);
                        return;
                    }
                    console.log("temp 2 : " + msg.body );
                    $scope.temp2 = msg.body;
                    $scope.$apply();
                });

            };

            $scope.eb.onclose = function() {
                $scope.eb = null;
            };

            $scope.$apply();
        });

    }]);
