## Just notes

`copy( issuesList.map(issue => ({issue: {issueType: issue.caption, issueDescription: issue.description}, type: issue.part, pattern: '.*' + issue.pattern + '.*'})))`

`mvn clean compile assembly:single`