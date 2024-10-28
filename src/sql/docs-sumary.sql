SELECT 
    d.id, d.title, a.name, t.name, f.code, d.rating
FROM
    `mydocs-prod`.doc d
        LEFT OUTER JOIN
    doc_author a ON a.id = d.main_author_id
     LEFT OUTER JOIN doc_format f on f.id=d.format_id
     left outer join doc_topic t on t.id=d.maintopic_id