let server_addr="http://127.0.0.1:7090/";
// let server_addr="http://10.11.9.25:7090/";

let reqDubbo="dubbo-invoker/index";
let reqServiceAddr="dubbo-invoker/get-service-name-list?serviceAddr=";
let selectArgRecord="dubbo-invoke-req-record/select-in-page";
let saveArg="dubbo-invoke-req-record/save-one";
let userLogin="dubbo-invoker/login";
let userRegister="dubbo-invoker/register";
let userInfo = "dubbo-invoker/get-user-info";
let zkHost = "dubbo-invoker/get-all-register";
let changeTestCase = "dubbo-invoker/change-req-arg";
let selectAllUser = "dubbo-invoker/get-all-user";
let selectMethodName = "dubbo-invoker/get-method-list";
let queryMetaData = "dubbo-invoker/get-meta-data";

function getQueryMetaDataUrl() {
    return server_addr + queryMetaData;
}
function getSelectMethodNameUrl() {
    return server_addr + selectMethodName;
}
function getChangeTestCaseUrl() {
    return server_addr + changeTestCase;
}

function getSelectAllUser() {
    return server_addr + selectAllUser;
}

function getZkHost() {
    return server_addr + zkHost;
}

function getServiceAddr(){
    return server_addr+reqServiceAddr;
}

function getReqDubboUrl() {
    return server_addr + reqDubbo;
}

function getSelectArgRecordUrl(){
    return server_addr+selectArgRecord;
}

function getSaveArgRecordUrl() {
    return server_addr+saveArg;
}

function getUserRegisterUrl(){
    return server_addr+userRegister;
}

function getUserLoginUrl() {
    return server_addr+userLogin;
}

function getUserInfoUrl() {
    return server_addr+userInfo;
}