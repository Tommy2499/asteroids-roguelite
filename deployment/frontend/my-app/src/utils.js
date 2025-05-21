export function form10(value){
    if (value < 10){
        value = '0' + value;
    }
    return value;
}

export function sToMin(seconds){
    let min = Math.floor(seconds / 60);
    let rem = seconds % 60;
    return {'min': min, 'rem': rem};
}

export function sToHr(seconds) {
    let hr = Math.floor(seconds / 3600);
    let rem = seconds % 3600;
    return {'hr': hr, 'rem': rem};
}

export function formatTime(seconds) {
    seconds = Number(seconds);
    if (seconds < 60){
        return '00:' + form10(seconds);
    }
    else if (seconds < 3600){
        let minRem = sToMin(seconds);
        return form10(minRem.min) + ':' + form10(minRem.rem);
    }
    else {
        let hrRem = sToHr(seconds);
        let minRem = sToMin(hrRem.rem);
        return formatLongNumber(hrRem.hr) + ':' + form10(minRem.min) + ':' + form10(minRem.rem);
    }
}

export function formatLongNumber(num) {
    num = num.toString();
    return num.replace(/\B(?=(\d{3})+(?!\d))/g, " ");
}