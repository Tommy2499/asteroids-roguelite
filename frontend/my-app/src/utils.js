/**
 * Adds a leading zero to numbers less than 10 to ensure two-digit formatting.
 * 
 * @param {number} value - The number to format.
 * @returns {string} The formatted number as a string.
 */
export function form10(value) {
    if (value < 10) {
        value = '0' + value;
    }
    return value;
}

/**
 * Converts seconds into minutes and remaining seconds.
 * 
 * @param {number} seconds - The total number of seconds.
 * @returns {Object} An object containing minutes (`min`) and remaining seconds (`rem`).
 */
export function sToMin(seconds) {
    let min = Math.floor(seconds / 60);
    let rem = seconds % 60;
    return { 'min': min, 'rem': rem };
}

/**
 * Converts seconds into hours and remaining seconds.
 * 
 * @param {number} seconds - The total number of seconds.
 * @returns {Object} An object containing hours (`hr`) and remaining seconds (`rem`).
 */
export function sToHr(seconds) {
    let hr = Math.floor(seconds / 3600);
    let rem = seconds % 3600;
    return { 'hr': hr, 'rem': rem };
}

/**
 * Formats a time value given in seconds into a human-readable string.
 * 
 * - If the time is less than 60 seconds, it returns `00:SS`.
 * - If the time is less than 3600 seconds, it returns `MM:SS`.
 * - If the time is 3600 seconds or more, it returns `HH:MM:SS`.
 * 
 * @param {number} seconds - The total number of seconds.
 * @returns {string} The formatted time string.
 */
export function formatTime(seconds) {
    seconds = Number(seconds);
    if (seconds < 60) {
        return '00:' + form10(seconds);
    } else if (seconds < 3600) {
        let minRem = sToMin(seconds);
        return form10(minRem.min) + ':' + form10(minRem.rem);
    } else {
        let hrRem = sToHr(seconds);
        let minRem = sToMin(hrRem.rem);
        return formatLongNumber(hrRem.hr) + ':' + form10(minRem.min) + ':' + form10(minRem.rem);
    }
}

/**
 * Formats a large number by adding spaces as thousand separators.
 * 
 * Example: 1234567 becomes "1 234 567".
 * 
 * @param {number|string} num - The number to format.
 * @returns {string} The formatted number as a string.
 */
export function formatLongNumber(num) {
    num = num.toString();
    return num.replace(/\B(?=(\d{3})+(?!\d))/g, " ");
}