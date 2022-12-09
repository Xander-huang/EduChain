/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.zy68.personservice.pojo.uservo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ReqSignHashVo.
 */
@Data
public class ReqSignHashVo {
    @NotBlank(message = "signUserId cannot be empty")
    private String signUserId;
    @NotBlank(message = "hashStr cannot be empty")
    private String messageHash;
    /**
     * 0:ecdsa, 1:sm
     */
    @NotNull
    private Integer encryptType;
}
