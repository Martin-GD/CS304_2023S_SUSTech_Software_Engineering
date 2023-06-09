package com.team127.atom.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Tag {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tag.id
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tag.name
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tag.color
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private String color;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tag.description
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    private String description;

    private String creator;

}