var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'AppMarketUpdate', 'coolMethod', [arg0]);
};

exports.updateDevice = function(arg0, success, error) {
    exec(success, error, 'AppMarketUpdate', 'updateDevice', [arg0] );
};

/*
module.exports = {s
    updateDevice
} */