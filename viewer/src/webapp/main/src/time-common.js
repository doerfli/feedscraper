import moment from "moment";

function formatDateTimeLong(timestamp) {
    return moment(timestamp).format('MMMM Do YYYY, HH:mm');
}

function formatDateTimeShort(timestamp) {
    return moment(timestamp).format('YYYY/MM/DD HH:mm');
}

function formatDateTimeFromNow(timestamp) {
    return moment(timestamp).fromNow();
}

export {
    formatDateTimeLong,
    formatDateTimeShort,
    formatDateTimeFromNow
};
