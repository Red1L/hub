/*
 * Copyright (c) 2015-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
define([
    '{angular}/angular',
    '{angular-resource}/angular-resource'

], function (angular) {
    'use strict';

    var module = angular.module('home', ['ngResource']);

    module.controller('HomeController', ['$scope', '$location', function ($scope, $location) {
        var home = this;

        home.details = function () {
            $location.path('hub/component/name');
        };
    }]);

    return {
        angularModules: ['home']
    };
})
;
