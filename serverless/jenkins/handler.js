'use strict';

const urlTest = /polymer-qa[0-9]?[0-9]?\.dev\.gloo\.us|polymer-restricted[0-9]?[0-9]?\.dev\.gloo\.us|polymer-uitest[0-9]?[0-9]?\.dev\.gloo\.us/

module.exports.jenkinsInfo = (event, context, callback) => {
    const jenkins = require('jenkins')({
        baseUrl: process.env.GLOO_RESOURCE_ALLOC_JENKINS_URL,
        crumbIssuer: true
    });
    jenkins.info((err, { jobs }) => {
        if (err) throw err;
        const filteredResources = jobs.filter(({ name }) => urlTest.test(name));
        const response = {
            statusCode: 200,
            body: JSON.stringify({
                message: filteredResources,
                input: event,
            }),
        };
        callback(null, response);
    });
    // Use this code if you don't use the http event with the LAMBDA-PROXY integration
    // callback(null, { message: 'Go Serverless v1.0! Your function executed successfully!', event });
};
