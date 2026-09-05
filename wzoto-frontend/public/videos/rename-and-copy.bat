@echo off
chcp 65001 >nul
echo ========================================
echo  百度网盘视频文件重命名脚本
echo  请在下载完视频后运行此脚本
echo ========================================
echo.

set "SRC=%USERPROFILE%\Downloads"
set "DST=%~dp0"

echo 源目录: %SRC%
echo 目标目录: %DST%
echo.

REM === 数学 (29个文件) ===
echo [1/3] 处理数学文件...
if exist "%DST%grade4-math" (echo grade4-math 已存在) else mkdir "%DST%grade4-math"
for %%f in (
  "前言.MP4"
  "亿以内数的认识（一）.MP4"
  "亿以内数的认识（二）.MP4"
  "数的产生和十进制计数法.MP4"
  "亿以上数的认识.MP4"
  "计算机工具的认识.MP4"
  "一亿有多大.MP4"
  "公顷和平方千米.MP4"
  "直线、射线、线段.MP4"
  "角的度量.MP4"
  "角的分类.MP4"
  "角.MP4"
  "笔算乘法.MP4"
  "中间或末尾有0的乘法.MP4"
  "积的变化规律和估算.MP4"
  "单价公式和路程公式.MP4"
  "平行与垂直（一）.MP4"
  "平行与垂直（二）.MP4"
  "平行四边形和梯形.MP4"
  "口算除法.MP4"
  "除数接近整十的除法.MP4"
  "除数为两位数的除法.MP4"
  "商的变化规律.MP4"
  "条形统计图.MP4"
  "数学广角（一）.MP4"
  "数学广角（二）.MP4"
  "总复习（一）.MP4"
  "总复习（二）.MP4"
  "总复习（三）.MP4"
) do (
  if exist "%SRC%\%%~f" (
    copy "%SRC%\%%~f" "%DST%grade4-math\%%~f" >nul
    echo   OK: %%~f
  ) else (
    echo   MISSING: %%~f
  )
)

echo.
echo [2/3] 处理语文文件...
if exist "%DST%grade4-chinese" (echo grade4-chinese 已存在) else mkdir "%DST%grade4-chinese"
echo   语文81个文件请手动从百度网盘下载后复制到 grade4-chinese 文件夹

echo.
echo [3/3] 处理英语文件...
if exist "%DST%grade4-english" (echo grade4-english 已存在) else mkdir "%DST%grade4-english"
for %%f in (
  "Unit 1-1.MP4" "Unit 1-2.MP4" "Unit 1-3.MP4" "Unit 1-4.MP4" "Unit 1-5.MP4" "Unit 1-6.MP4"
  "Unit 2-1.MP4" "Unit 2-2.MP4" "Unit 2-3.MP4" "Unit 2-4.MP4" "Unit 2-5.MP4" "Unit 2-6.MP4"
  "Unit 3-1.MP4" "Unit 3-2.MP4" "Unit 3-3.MP4" "Unit 3-4.MP4" "Unit 3-5.MP4" "Unit 3-6.MP4"
  "Unit 4-1.MP4" "Unit 4-2.MP4" "Unit 4-3.MP4" "Unit 4-4.MP4" "Unit 4-5.MP4" "Unit 4-6.MP4"
  "Unit 5-1.MP4" "Unit 5-2.MP4" "Unit 5-3.MP4" "Unit 5-4.MP4" "Unit 5-5.MP4" "Unit 5-6.MP4"
  "Unit 6-1.MP4" "Unit 6-2.MP4" "Unit 6-3.MP4" "Unit 6-4.MP4" "Unit 6-5.MP4" "Unit 6-6.MP4"
  "Recycle 1-1.MP4" "Recycle 1-2.MP4" "Recycle 1-3.MP4"
  "Recycle 2-1.MP4" "Recycle 2-2.MP4" "Recycle 2-3.MP4"
) do (
  if exist "%SRC%\%%~f" (
    copy "%SRC%\%%~f" "%DST%grade4-english\%%~f" >nul
    echo   OK: %%~f
  ) else (
    echo   MISSING: %%~f
  )
)

echo.
echo ========================================
echo  完成！请将 grade4-math / grade4-chinese / grade4-english 文件夹
echo  上传到 Cloudflare R2 或前端 public/videos/ 目录
echo ========================================
pause
