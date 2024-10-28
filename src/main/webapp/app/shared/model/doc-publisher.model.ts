import { IDoc } from '@/shared/model/doc.model';
import { IDocCollection } from '@/shared/model/doc-collection.model';

export interface IDocPublisher {
  id?: number;
  name?: string | null;
  notes?: string | null;
  url?: string | null;
  docs?: IDoc[] | null;
  collections?: IDocCollection[] | null;
}

export class DocPublisher implements IDocPublisher {
  constructor(
    public id?: number,
    public name?: string | null,
    public notes?: string | null,
    public url?: string | null,
    public docs?: IDoc[] | null,
    public collections?: IDocCollection[] | null
  ) {}
}
