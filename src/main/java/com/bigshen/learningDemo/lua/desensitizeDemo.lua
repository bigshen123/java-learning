-- 脱敏手机号
function desensitizePhoneNumber(phoneNumber)
    if string.len(phoneNumber) == 11 then
        local prefix = string.sub(phoneNumber, 1, 3)
        local suffix = string.sub(phoneNumber, 9, 11)
        return prefix .. "****" .. suffix
    else
        return "手机号格式不正确"
    end
end

-- 脱敏姓名
function desensitizeName(name)
    local nameLength = SubStringGetTotalIndex(name)
    if nameLength == 2 then
        return SubStringUTF8(name, 2, 2)
    elseif nameLength == 3 then
        return SubStringUTF8(name, 1, 2)
    elseif nameLength > 3 then
        return SubStringUTF8(name, 1, 3)
    else
        return "姓名格式不正确"
    end
end

--截取中英混合的UTF8字符串，endIndex可缺省
function SubStringUTF8(str, startIndex, endIndex)
    if startIndex < 0 then
        startIndex = SubStringGetTotalIndex(str) + startIndex + 1;
    end

    if endIndex ~= nil and endIndex < 0 then
        endIndex = SubStringGetTotalIndex(str) + endIndex + 1;
    end

    if endIndex == nil then
        return string.sub(str, SubStringGetTrueIndex(str, startIndex));
    else
        return string.sub(str, SubStringGetTrueIndex(str, startIndex), SubStringGetTrueIndex(str, endIndex + 1) - 1);
    end
end

--获取中英混合UTF8字符串的真实字符数量
function SubStringGetTotalIndex(str)
    local curIndex = 0;
    local i = 1;
    local lastCount = 1;
    repeat
        lastCount = SubStringGetByteCount(str, i)
        i = i + lastCount;
        curIndex = curIndex + 1;
    until(lastCount == 0);
    return curIndex - 1;
end

function SubStringGetTrueIndex(str, index)
    local curIndex = 0;
    local i = 1;
    local lastCount = 1;
    repeat
        lastCount = SubStringGetByteCount(str, i)
        i = i + lastCount;
        curIndex = curIndex + 1;
    until(curIndex >= index);
    return i - lastCount;
end

--返回当前字符实际占用的字符数
function SubStringGetByteCount(str, index)
    local curByte = string.byte(str, index)
    local byteCount = 1;
    if curByte == nil then
        byteCount = 0
    elseif curByte > 0 and curByte <= 127 then
        byteCount = 1
    elseif curByte>=192 and curByte<=223 then
        byteCount = 2
    elseif curByte>=224 and curByte<=239 then
        byteCount = 3
    elseif curByte>=240 and curByte<=247 then
        byteCount = 4
    end
    return byteCount;
end

-- 测试脱敏函数
local phoneNumber = "18912345670"
local name1 = "李傲"
local name2 = "巴永杰"
local name3 = "欧阳峰峰"
local name4 = "test"
local name5 = "test123巴永杰"

print("脱敏后的手机号: " .. desensitizePhoneNumber(phoneNumber))
print("脱敏后的姓名1: " .. desensitizeName(name1))
print("脱敏后的姓名2: " .. desensitizeName(name2))
print("脱敏后的姓名3: " .. desensitizeName(name3))
print("脱敏后的姓名4: " .. desensitizeName(name4))
print("脱敏后的姓名5: " .. desensitizeName(name5))