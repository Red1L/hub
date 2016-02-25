/// <amd-dependency path="{w20-core}/modules/hypermedia"/>
/// <amd-dependency path="{hub}/modules/model/components"/>
/// <amd-dependency path="{angular-messages}/angular-messages"/>
/// <amd-dependency path="{angular-material}/angular-material"/>
import angular = require("{angular}/angular");
angular.module('Hub.controllers', ['w20CoreHypermedia', 'ngMaterial', 'ngMessages']);
export = { angularModules: 'Hub.controllers' }