/**
 * Authority rule service.
 */
angular.module('sentinelDashboardApp').service('AuthorityRuleService', ['$http', function ($http) {
	this.queryMachineRules = function (app, ip, port) {
        var param = {
            app: app,
            ip: ip,
            port: port
        };
        return $http({
            url: '/v2/authority/rules',
            params: param,
            method: 'GET'
        });
    };

    this.newRule = function (rule) {
        return $http({
            url: '/v2/authority/rule',
            data: rule,
            method: 'POST'
        });
    };

    this.saveRule = function (rule) {
        return $http({
            url: '/v2/authority/rule/' + rule.id,
            data: rule,
            method: 'PUT'
        });
    };

    this.deleteRule = function (rule) {
        return $http({
            url: '/v2/authority/rule/' + rule.id,
            method: 'DELETE'
        });
    };

    this.checkRuleValid = function checkRuleValid(rule) {
        if (rule.resource === undefined || rule.resource === '') {
            window.alert('资源名称不能为空');
            return false;
        }
        if (rule.limitApp === undefined || rule.limitApp === '') {
            window.alert('流控针对应用不能为空');
            return false;
        }
        if (rule.strategy === undefined) {
            window.alert('必须选择黑白名单模式');
            return false;
        }
        return true;
    };
}]);
