'use strict';

const qaTest = /polymer-qa[0-9]?[0-9]?\.dev\.gloo\.us/;
const reTest = /polymer-restricted[0-9]?[0-9]?\.dev\.gloo\.us/;
const uiTest = /polymer-uitest[0-9]?[0-9]?\.dev\.gloo\.us/;

const matchAnyTest = (name) => qaTest.test(name) || reTest.test(name) || uiTest.test(name);

module.exports.jenkinsInfo = (event, context, callback) => {
    const jenkins = require('jenkins')({
        baseUrl: process.env.GLOO_RESOURCE_ALLOC_JENKINS_URL,
        crumbIssuer: true
    });
    jenkins.info((err, {jobs}) => {
        if (err) throw new Error(err);
        const filteredResources = jobs
            .filter(({ name }) => matchAnyTest(name))
            .map((resource) => {
                const { name } = resource;
                const [ ordinal ] = name.split('.')[0].match(/\d+/);
                resource.ordinal = +ordinal;
                if (qaTest.test(name)) {
                    resource.type = 'qa';
                } else if (reTest.test(name)) {
                    resource.type = 'restricted';
                } else {
                    resource.type = 'uitest';
                }
                return {[resource.name]: resource};
            })
            .reduce((a, b) => Object.assign(a, b), {});
        return callback(null, filteredResources); // uses lambda proxy integration
    });
};