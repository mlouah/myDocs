import { IDocPublisher } from '@/shared/model/doc-publisher.model';
import { IDocCollection } from '@/shared/model/doc-collection.model';
import { IDocFormat } from '@/shared/model/doc-format.model';
import { ILanguage } from '@/shared/model/language.model';
import { IDocTopic } from '@/shared/model/doc-topic.model';
import { IDocAuthor } from '@/shared/model/doc-author.model';
import { IDocCategory } from '@/shared/model/doc-category.model';

export interface IDoc {
  id?: number;
  title?: string;
  subTitle?: string | null;
  publishYear?: string | null;
  coverImgPath?: string | null;
  editionNumer?: number | null;
  summary?: string | null;
  purchaseDate?: Date | null;
  startReadingDate?: Date | null;
  endReadingDate?: Date | null;
  price?: number | null;
  copies?: number | null;
  pageNumber?: number | null;
  numDoc?: string | null;
  myNotes?: string | null;
  keywords?: string | null;
  toc?: string | null;
  filename?: string | null;
  publisher?: IDocPublisher | null;
  collection?: IDocCollection | null;
  format?: IDocFormat | null;
  langue?: ILanguage | null;
  maintopic?: IDocTopic | null;
  mainAuthor?: IDocAuthor | null;
  docCategory?: IDocCategory | null;
}

export class Doc implements IDoc {
  constructor(
    public id?: number,
    public title?: string,
    public subTitle?: string | null,
    public publishYear?: string | null,
    public coverImgPath?: string | null,
    public editionNumer?: number | null,
    public summary?: string | null,
    public purchaseDate?: Date | null,
    public startReadingDate?: Date | null,
    public endReadingDate?: Date | null,
    public price?: number | null,
    public copies?: number | null,
    public pageNumber?: number | null,
    public numDoc?: string | null,
    public myNotes?: string | null,
    public keywords?: string | null,
    public toc?: string | null,
    public filename?: string | null,
    public publisher?: IDocPublisher | null,
    public collection?: IDocCollection | null,
    public format?: IDocFormat | null,
    public langue?: ILanguage | null,
    public maintopic?: IDocTopic | null,
    public mainAuthor?: IDocAuthor | null,
    public docCategory?: IDocCategory | null
  ) {}
}
