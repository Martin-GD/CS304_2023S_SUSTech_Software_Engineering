create table tag
(
    id          VARCHAR(100)
        primary key,
    name        varchar(20)                   not null,
    color       varchar(20) default '#FFFFFF' not null,
    description text                          null,
    creator     varchar(100) default 0
);

create table user
(
    id         varchar(100)
        primary key,
    name       varchar(30)  null,
    email      text         null,
    bio        varchar(200) null,
    location   varchar(100) null,
    company    varchar(50)  null,
    password   text         not null,
    created_at bigint       not null,
    updated_at bigint       not null,
    auth       int          not null
);

create table collection
(
    id    VARCHAR(100)
        primary key,
    ownerId varchar(100)           not null,
    name  varchar(20)   not null,
    auth  int default 1 not null,
    constraint fk__ds_collection__owner_id
        foreign key (ownerId) references user (id)
            on delete cascade
);

create table collection_tag
(
    collection VARCHAR(100) not null,
    tag        VARCHAR(100) not null,
    primary key (collection, tag),
    constraint fk__rel__collection_tag_id
        foreign key (tag) references tag (id)
            on delete cascade,
    constraint fk__rel__tag_collection_id
        foreign key (collection) references collection (id)
            on delete cascade
);
create table issue
(
    id         VARCHAR(100)
        primary key,
    opener     VARCHAR(100)           not null,
    title      varchar(100)  not null,
    star       int default 0 not null,
    love       int default 0 not null,
    isDraft       int default 0 not null,
    content    text          null,
    createdAt bigint        not null,
    constraint fk__rel_user_issue_id
        foreign key (opener) references user (id)
            on delete cascade
);

create table note
(
    id         varchar(100)
        primary key,
    owner      VARCHAR(100)  not null,
    collection VARCHAR(100)  not null,
    name       varchar(20)   not null,
    auth       int default 0 not null,
    created_at bigint        not null,
    updated_at bigint        not null,
    description varchar(100) not null,
    constraint fk__rel_user_note_id
        foreign key (owner) references user (id)
            on delete cascade,
    constraint fk__rel_repo_note_id
        foreign key (collection) references atom.collection (id)
            on delete cascade
);

create table reply
(
    id         VARCHAR(100)
        primary key,
    author     VARCHAR(100)           not null,
    issue      VARCHAR(100)           not null,
    star       int default 0 not null,
    comment    text          null,
    created_at bigint        not null,
    constraint fk__rel_issue_reply_id
        foreign key (issue) references issue (id)
            on delete cascade,
    constraint fk__rel_user_reply_id
        foreign key (author) references user (id)
            on delete cascade
);

create table comment
(
    id         VARCHAR(100)
        primary key,
    author     VARCHAR(100)           not null,
    reply      VARCHAR(100)           not null,
    star       int default 0 not null,
    content    text          null,
    created_at bigint        not null,
    constraint fk__rel_reply_comment_id
        foreign key (reply) references reply (id)
            on delete cascade,
    constraint fk__rel_user_comment_id
        foreign key (author) references user (id)
            on delete cascade
);

create table user_star_collection
(
    collection VARCHAR(100) not null,
    user       varchar(100) not null,
    constraint uniq__rel_user_star__record
        unique (collection, user),
    constraint fk__rel_user_star__collection_id
        foreign key (collection) references collection (id)
            on delete cascade,
    constraint fk__rel_user_star__user_id
        foreign key (user) references user (id)
            on delete cascade
);

create table user_star_note
(
    note varchar(100) not null,
    user varchar(100) not null,
    constraint uniq__rel_user_note_star__record
        unique (note, user),
    constraint fk__rel_note_star__user_id
        foreign key (user) references user (id)
            on delete cascade,
    constraint fk__rel_user_star__note_id
        foreign key (note) references note (id)
            on delete cascade
);

create table user_star_issue
(
    user  varchar(100) not null,
    issue varchar(100) not null,
    constraint user_star_issue_pk
        unique (user, issue),
    constraint fk__rel_issue_star__user_id
        foreign key (user) references user (id)
            on delete cascade,
    constraint fk__rel_user_star__issue_id
        foreign key (issue) references issue (id)
            on delete cascade
);

create table user_like_issue
(
    user  varchar(100) not null,
    issue varchar(100) not null,
    constraint user_like_issue_pk
        unique (user, issue),
    constraint fk__rel_issue_like__user_id
        foreign key (user) references user (id)
            on delete cascade,
    constraint fk__rel_user_like__issue_id
        foreign key (issue) references issue (id)
            on delete cascade
);

create table user_like_reply
(
    user  varchar(100) not null,
    reply varchar(100) not null,
    constraint user_like_reply_pk
        unique (user, reply),
    constraint fk__rel_reply_like__user_id
        foreign key (user) references user (id)
            on delete cascade,
    constraint fk__rel_user_like__reply_id
        foreign key (reply) references reply (id)
            on delete cascade
);

create table user_star_comment
(
    user  varchar(100) not null,
    comment varchar(100) not null,
    constraint user_like_comment_pk
        unique (user, comment),
    constraint fk__rel_comment_like__user_id
        foreign key (user) references user (id)
            on delete cascade,
    constraint fk__rel_user_like__comment_id
        foreign key (comment) references comment (id)
            on delete cascade
);
create table note_tag
(
    note VARCHAR(100) not null,
    tag        VARCHAR(100) not null,
    primary key (note, tag),
    constraint fk__rel__note_tag_id
        foreign key (tag) references tag (id)
            on delete cascade,
    constraint fk__rel__tag_note_id
        foreign key (note) references note (id)
            on delete cascade
);
create index user_name
    on user (name);
create table if not exists atom.chat_user_link
(
    link_id    varchar(100) not null
    primary key,
    from_user  varchar(30)  null,
    to_user    varchar(30)  null,
    created_at datetime     null,
    constraint chat_user_link_user_name_fk
    foreign key (from_user) references atom.user (name)
    on delete cascade,
    constraint chat_user_link_user_name_fk_2
    foreign key (to_user) references atom.user (name)
    on delete cascade
    );

create table if not exists atom.chat_list
(
    list_id     int auto_increment comment '聊天列表主键'
    primary key,
    link_id     varchar(100) not null comment '聊天主表id',
    from_user   varchar(30)  not null comment '发送者',
    to_user     varchar(30)  not null comment '接收者',
    from_window tinyint      null comment '发送方是否在窗口',
    to_window   tinyint      null comment '接收方是否在窗口',
    unread      int          null comment '未读数',
    status      tinyint      null comment '是否删除',
    constraint fk_list_link
    foreign key (link_id) references atom.chat_user_link (link_id),
    constraint fk_list_user1
    foreign key (from_user) references atom.user (name),
    constraint fk_list_user2
    foreign key (to_user) references atom.user (name)
    );

create table if not exists atom.chat_message
(
    message_id int auto_increment comment '聊天内容id'
    primary key,
    link_id    varchar(100)  not null comment '聊天主表id',
    from_user  varchar(30)   not null comment '发送者',
    to_user    varchar(30)   not null comment '接收者',
    content    varchar(255)  not null comment '聊天内容',
    send_time  datetime      not null comment '发送时间',
    type       int default 0 not null comment '消息类型',
    is_latest  tinyint       null comment '是否为最后一条信息',
    constraint fk_message_link
    foreign key (link_id) references atom.chat_user_link (link_id),
    constraint fk_message_user1
    foreign key (from_user) references atom.user (name),
    constraint fk_message_user2
    foreign key (to_user) references atom.user (name)
    );
