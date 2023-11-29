---
name: Bug report
about: Report bug or performance issues
labels: [Low,Medium,High,Vulnerability]

---

**Description**
Briefly describe the bug

---

**Steps to reproduce**
1. Send POST to /example

Request:
```
POST /example HTTP/1.1
Host: localhost:9090
User-Agent: curl/8.4.0
Accept: */*
Content-Length: 17
Content-Type: application/x-www-form-urlencoded

{"param":"value"}
```
2. ...
3. ...

...

---

**Result**
When action A is done, B happens

**Expected Result**
When action A is performed, it must be B

---
[!!] *Don't forget to attach logs ([guide](https://github.com/Toolll1/document-flow/tree/BugReportTemplate#%D0%BA%D0%B0%D0%BA-%D1%81%D0%BD%D1%8F%D1%82%D1%8C-%D0%BB%D0%BE%D0%B3%D0%B8-%D1%81-%D0%BA%D0%BE%D0%BD%D1%82%D0%B5%D0%B9%D0%BD%D0%B5%D1%80%D0%B0))*
