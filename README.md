# permission
A flexible, visual permission system developed independently without Spring Security and Apache Shiro.

## Brief intro
This is an enterprise permission system, with flexible, visual, fine-grained features without using any existing mature permission system like Spring Security and Apache Shiro.

## Core tech stack
1. On the back end, it uses Spring, SpringMVC, Mybatis, MySQL, Redis, managed by maven, drivered by tomcat, with Google's Guava and Alibaba's Druid.
2. On the front end, it uses HTML, CSS, jQuery, Bootstrap, Mustache, zTree, Duallistbox, ajax.

## Features
1. It's developed for enterprise permission demand.
2. It has visual interface, convenient for quick and clear management of departments, users, permissions, roles, role-permission relationship, role-user relationship.
3. Most importantly, it has a visual log module, in which any operaions can be queried, and it allows revoking misoperations.

## Why independent ?
As we know, there are two mature enterprise perssion systems already as far, Spring Secuity and Apache Shiro, so why not using them ? There are serveral reasons:
1. Dispite Spring Secruity has many advantages like security insurance, user authentication, spring-based. However, it has many configuration files, RBAC characteristic is not that obvious. It's not visual for management. And in the case of large amount of data, it's almost unavailable.
2. Apache Shiro, compared with Spring Security, it's much easier to use, with flexible operation for dealing demand and web service, and easy to be integrated with other frameworks like Spring. However, its comunity is not that active, lacks learning materials, and same as Spring Security, it doesn't has a front end visual interface.

