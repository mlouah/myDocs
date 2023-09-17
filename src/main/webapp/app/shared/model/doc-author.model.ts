import { IDoc } from '@/shared/model/doc.model';

export interface IDocAuthor {
  id?: number;
  name?: string | null;
  imgUrl?: string | null;
  notes?: string | null;
  url?: string | null;
  docs?: IDoc[] | null;
}

export class DocAuthor implements IDocAuthor {
  constructor(
    public id?: number,
    public name?: string | null,
    public imgUrl?: string | null,
    public notes?: string | null,
    public url?: string | null,
    public docs?: IDoc[] | null
  ) {}
}
