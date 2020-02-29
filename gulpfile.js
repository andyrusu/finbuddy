const { series, src, dest } = require('gulp');
const del = require('del');

function cleanAll()
{
    return del([
        "dist/browser/**/*",
        "dist/devcards/**/*",
        "dist/electron/**/*",
        "dist/mobile/**/*",
    ]);
}

function cleanBrowser()
{
    return del("dist/browser/**/*");
}

function buildBrowser() 
{
    return src('assets/**/*')
          .pipe(dest('dist/browser'));
}

function cleanDevcards()
{
    return del("dist/devcards/**/*");
}

function buildDevcards() 
{
    return src('assets/**/*')
          .pipe(dest('dist/devcards'));
}

exports.default = cleanAll;
exports.cleanBrowser = cleanBrowser;
exports.browser = series(cleanBrowser, buildBrowser);
exports.cleanDevcards = cleanDevcards;
exports.devcards = series(cleanDevcards, buildDevcards);