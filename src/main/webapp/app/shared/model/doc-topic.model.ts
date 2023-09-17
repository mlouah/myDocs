import { IDoc } from '@/shared/model/doc.model';
import { IDomaine } from '@/shared/model/domaine.model';

export interface IDocTopic {
  id?: number;
  name?: string | null;
  code?: string | null;
  imgUrl?: string | null;
  notes?: string | null;
  docs?: IDoc[] | null;
  domaine?: IDomaine | null;
}

export class DocTopic implements IDocTopic {
  constructor(
    public id?: number,
    public name?: string | null,
    public code?: string | null,
    public imgUrl?: string | null,
    public notes?: string | null,
    public docs?: IDoc[] | null,
    public domaine?: IDomaine | null
  ) {}
}
