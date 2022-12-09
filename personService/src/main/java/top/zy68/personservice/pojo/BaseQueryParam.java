package top.zy68.personservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseQueryParam {
    private Integer start;
    private Integer pageSize;
}